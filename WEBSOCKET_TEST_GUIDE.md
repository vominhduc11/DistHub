# 🚀 WebSocket Test Guide

## Mở file test
Mở file `websocket-test.html` bằng trình duyệt web (Chrome, Firefox, etc.)

## 📋 Hướng dẫn test từng bước

### Bước 1: Login
1. **Sử dụng các account có sẵn:**
   - Username: `testreseller03` - Password: `password123`
   - Username: `testreseller04` - Password: `password123`
2. **Hoặc tạo account mới bằng cách gọi API:**
   ```bash
   curl -X POST http://localhost:8081/api/auth/register-reseller \
     -H "Content-Type: application/json" \
     -d '{"username":"testuser05","password":"password123","name":"Test User 5","address":"123 Test St","district":"Test District","city":"Test City","phone":"0901234567","email":"testuser05@example.com"}'
   ```
3. **Click "Login"** - sẽ nhận được JWT token

### Bước 2: Connect WebSocket
1. **Click "Connect WebSocket"** 
2. **Kiểm tra status:** Sẽ hiện "✅ Connected to WebSocket as [username]"
3. **Xem logs:** Sẽ thấy STOMP debug messages

### Bước 3: Subscribe to Notifications
1. **Click "Subscribe to Personal Notifications"**
   - Subscribe đến `/user/queue/notifications`
   - Chỉ nhận notifications của chính user đó
2. **Click "Subscribe to Broadcast"** (optional)
   - Subscribe đến `/topic/notifications/broadcast` 
   - Nhận notifications broadcast cho tất cả users

### Bước 4: Test Notification
1. **Nhập test username và email** (ví dụ: `testuser06`, `testuser06@example.com`)
2. **Click "Register Test User"**
3. **Quan sát kết quả:**
   - Registration API thành công
   - **WebSocket notification sẽ xuất hiện trong section "Received Notifications"**
   - Logs sẽ show notification received

## ✅ Expected Results

### Khi test thành công, bạn sẽ thấy:

#### 1. Personal Notification nhận được:
```json
{
  "eventType": "RESELLER_REGISTRATION",
  "userId": "testuser06",
  "message": "New reseller registration",
  "timestamp": "..."
}
```

#### 2. Logs sẽ hiện:
```
[HH:MM:SS] 📨 Received personal notification!
[HH:MM:SS] 📨 Notification displayed: RESELLER_REGISTRATION for user testuser06
```

#### 3. Notification section sẽ có:
```
[HH:MM:SS] Personal Notification
Event: RESELLER_REGISTRATION
User ID: testuser06
Message: New reseller registration
```

## 🔧 Troubleshooting

### Nếu không nhận được notification:
1. **Check WebSocket connection** - phải thấy "Connected" status
2. **Check subscription** - phải subscribe thành công 
3. **Check logs** - xem có error gì không
4. **Check Docker services** - đảm bảo notification-service đang chạy

### Common Issues:
- **401 Unauthorized**: JWT token expired hoặc invalid
- **Connection failed**: Notification service không chạy
- **No notifications**: Chưa subscribe hoặc username mismatch

## 🎯 Principal-Based Security Demo

File test này demonstrate:
1. **JWT Authentication** - phải login trước khi connect WebSocket
2. **Principal-based routing** - Spring tự động route notification đến đúng user
3. **Security isolation** - user chỉ nhận notifications của chính họ
4. **Real-time notifications** - nhận ngay khi có event xảy ra

## 📊 Test Scenarios

### Scenario 1: Single User
- Login với 1 user
- Subscribe personal notifications  
- Register test user → nhận notification

### Scenario 2: Multiple Users (Advanced)
- Mở 2 browser tabs
- Login với 2 users khác nhau
- Test xem mỗi user chỉ nhận notification của họ

### Scenario 3: Broadcast Test
- Subscribe broadcast notifications
- Có thể test bằng cách gửi broadcast message (cần implement)

## 🎉 Success Criteria

✅ **WebSocket Test PASSED khi:**
1. Login thành công và nhận JWT
2. WebSocket connect thành công với JWT auth
3. Subscribe personal notifications thành công
4. Register test user trigger notification
5. **Nhận được notification trong real-time**

Đây chính là proof-of-concept cho WebSocket principal-based security system! 🚀