# WebSocket Security Usage Guide

## Overview
The notification service now includes JWT-based authentication and authorization for WebSocket connections.

## Security Features Implemented

### 1. JWT Authentication
- WebSocket connections must include `Authorization: Bearer <token>` header
- Token is validated using RSA public key from auth-service
- Invalid tokens are rejected at connection time

### 2. Subscription Authorization
- Users can only subscribe to their own notification topics
- Pattern: `/topic/notifications/{userId}` where userId must match authenticated user
- Broadcast topics `/topic/notifications/broadcast` are allowed for all authenticated users

### 3. CORS Protection
- Only allows connections from `http://localhost:3000` and `http://localhost:8080`
- No more wildcard (*) origins

## React Client Usage Example

```javascript
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

// Get JWT token from your auth system
const token = localStorage.getItem('jwtToken');
const userId = extractUserIdFromToken(token); // Extract from JWT claims

// Create WebSocket connection with authentication
const socket = new SockJS('http://localhost:8084/ws-notifications');
const stompClient = Stomp.over(socket);

// Set authentication header
stompClient.connectHeaders = {
    'Authorization': `Bearer ${token}`
};

// Connect and subscribe
stompClient.connect({}, 
    // Success callback
    (frame) => {
        console.log('Connected:', frame);
        
        // ✅ ALLOWED: Subscribe to own notifications
        stompClient.subscribe(`/topic/notifications/${userId}`, (message) => {
            const notification = JSON.parse(message.body);
            console.log('Received notification:', notification);
        });
        
        // ✅ ALLOWED: Subscribe to broadcast notifications
        stompClient.subscribe('/topic/notifications/broadcast', (message) => {
            const notification = JSON.parse(message.body);
            console.log('Broadcast notification:', notification);
        });
        
        // ❌ BLOCKED: This will throw SecurityException
        // stompClient.subscribe('/topic/notifications/other-user-id', callback);
    },
    // Error callback
    (error) => {
        console.error('WebSocket connection error:', error);
    }
);
```

## Security Benefits

### Before (Insecure):
```javascript
// ❌ Anyone could connect and listen
stompClient.connect({}, callback);
stompClient.subscribe('/topic/notifications/999', callback); // Could spy on others
```

### After (Secure):
```javascript
// ✅ Must authenticate with valid JWT
stompClient.connectHeaders = { 'Authorization': `Bearer ${validToken}` };
stompClient.connect({}, callback);
stompClient.subscribe('/topic/notifications/123', callback); // Only own notifications
```

## Error Handling

### Connection Errors:
- `SecurityException: Missing or invalid Authorization header`
- `SecurityException: Invalid JWT token`
- `SecurityException: Token missing username`

### Subscription Errors:
- `SecurityException: Access denied: Cannot subscribe to another user's notifications`

## Testing

### Valid Connection Test:
1. Get JWT token from auth-service login
2. Connect with `Authorization: Bearer <token>` header
3. Subscribe to `/topic/notifications/{your-userId}`
4. Should receive notifications successfully

### Security Test:
1. Try connecting without Authorization header → Should fail
2. Try connecting with invalid token → Should fail
3. Try subscribing to another user's notifications → Should fail

## Notes
- The `userId` in subscription path should match the username from JWT token
- For production, consider implementing user ID mapping if usernames differ from user IDs
- WebSocket security is independent of HTTP endpoint security