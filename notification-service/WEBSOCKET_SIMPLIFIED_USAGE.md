# Simplified WebSocket Usage (Principal-Based)

## Overview
WebSocket notifications now use Spring's principal-based routing, eliminating the need for clients to specify userId in subscriptions.

## React Client Usage

### Simple Subscription (Recommended)
```javascript
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const token = localStorage.getItem('jwtToken');

// Create WebSocket connection
const socket = new SockJS('http://localhost:8084/ws-notifications');
const stompClient = Stomp.over(socket);

// Set authentication header
stompClient.connectHeaders = {
    'Authorization': `Bearer ${token}`
};

// Connect and subscribe
stompClient.connect({}, 
    (frame) => {
        console.log('Connected:', frame);
        
        // ✅ Subscribe to personal notifications (no userId needed!)
        stompClient.subscribe('/user/queue/notifications', (message) => {
            const notification = JSON.parse(message.body);
            console.log('Personal notification:', notification);
        });
        
        // ✅ Subscribe to broadcast notifications
        stompClient.subscribe('/topic/notifications/broadcast', (message) => {
            const notification = JSON.parse(message.body);
            console.log('Broadcast notification:', notification);
        });
    },
    (error) => {
        console.error('WebSocket connection error:', error);
    }
);
```

## Available Destinations

### Personal Notifications
- **Destination**: `/user/queue/notifications`
- **Access**: Only authenticated user receives their own notifications
- **Security**: Automatic - Spring routes based on principal

### Broadcast Notifications  
- **Destination**: `/topic/notifications/broadcast`
- **Access**: All authenticated users receive these notifications
- **Use case**: System-wide announcements

## Security Benefits

### Before (Manual userId):
```javascript
// ❌ Client had to extract and specify userId
const userId = extractUserIdFromToken(token);
stompClient.subscribe(`/topic/notifications/${userId}`, callback);
```

### After (Principal-based):
```javascript
// ✅ Spring automatically routes to authenticated user
stompClient.subscribe('/user/queue/notifications', callback);
```

## How It Works

1. **Authentication**: Client sends JWT token in connect headers
2. **Principal Creation**: Server validates JWT and creates principal with username
3. **Automatic Routing**: Spring routes `/user/queue/notifications` to correct user session
4. **Security**: No way to receive another user's notifications

## Error Handling

### Connection Errors:
- `SecurityException: Authentication required` - Missing/invalid JWT
- `SecurityException: Access denied: Invalid subscription destination` - Invalid destination

### Valid Destinations:
- ✅ `/user/queue/notifications` - Personal notifications
- ✅ `/topic/notifications/broadcast` - Broadcast notifications  
- ❌ `/topic/notifications` - Not allowed
- ❌ `/topic/notifications/123` - Old format, not allowed

## Migration from Old Approach

### Old Way:
```javascript
const userId = extractUserIdFromToken(token);
stompClient.subscribe(`/topic/notifications/${userId}`, callback);
```

### New Way:
```javascript
stompClient.subscribe('/user/queue/notifications', callback);
```

## Server-Side Benefits

- **Simplified Code**: No manual userId validation needed
- **Better Security**: Spring handles user routing automatically  
- **Cleaner Architecture**: Leverages Spring WebSocket features
- **Principal-Based**: Full integration with Spring Security

The new approach is simpler, more secure, and follows Spring WebSocket best practices!