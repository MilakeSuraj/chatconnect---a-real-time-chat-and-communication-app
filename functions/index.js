const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendMessageNotification = functions.firestore
  .document("chatRooms/{roomId}/messages/{messageId}")
  .onCreate(async (snap, context) => {
    const message = snap.data();
    const roomId = context.params.roomId;
    const userName = message.sent_by_name || "Someone";
    const text = message.message || "";
    // Add logging for debugging
    console.log(`New message in room: ${roomId} by ${userName}: ${text}`);
    // Replace spaces and unsafe characters in topic name
    const safeRoomId = roomId.replace(/[^a-zA-Z0-9_]/g, "_");
    console.log(
      `New message in room: ${roomId} (topic: room_${safeRoomId}) by ${userName}: ${text}`
    );
    const payload = {
      notification: {
        title: "New message",
        body: `${userName} in ${roomId}: ${text}`,
      },
      data: {
        roomId: roomId,
        userName: userName,
        message: text,
      },
    };
    try {
      const response = await admin
        .messaging()
        .sendToTopic(`room_${safeRoomId}`, payload);
      console.log(`Notification sent to topic room_${safeRoomId}:`, response);
    } catch (error) {
      console.error("Error sending notification:", error);
    }
    return null;
  });
