/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */
const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const {setGlobalOptions} = require("firebase-functions/v2");
const admin = require("firebase-admin");
const {onRequest} = require("firebase-functions/https");
const logger = require("firebase-functions/logger");

// For cost control, you can set the maximum number of containers that can be
// running at the same time. This helps mitigate the impact of unexpected
// traffic spikes by instead downgrading performance. This limit is a
// per-function limit. You can override the limit for each function using the
// `maxInstances` option in the function's options, e.g.
// `onRequest({ maxInstances: 5 }, (req, res) => { ... })`.
// NOTE: setGlobalOptions does not apply to functions using the v1 API. V1
// functions should each use functions.runWith({ maxInstances: 10 }) instead.
// In the v1 API, each function can only serve one request per container, so
// this will be the maximum concurrent request count.
setGlobalOptions({ maxInstances: 10 });

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });


admin.initializeApp();
const db = admin.firestore();

exports.sendNotificationOnCreate = onDocumentCreated("Notifications/{notifId}", async (event) => {
  const snap = event.data;
  if (!snap) return;
  const notif = snap.data();

  const userId = notif.userId;
  const title = notif.title || "Notification";
  const message = notif.message || "";

  if (!userId) {
    console.log("Missing userId");
    return;
  }

  const userSnap = await db.collection("Users").doc(userId).get();
  if (!userSnap.exists) return;

  const token = userSnap.get("notificationToken"); 
  if (!token) {
    console.log("User has no notificationToken");
    return;
  }

  try {
    const response = await admin.messaging().send({
      token: token,
      notification: {
        title: title,
        body: message
      },
      android: {
        priority: "high"
      },
      data: {
        notifId: event.params.notifId,
        userId: userId
      }
    });

    console.log("FCM sent:", response);

    await snap.ref.update({
      sent: true,
      timestamp: admin.firestore.FieldValue.serverTimestamp()
    });

  } catch (err) {
    console.error("Error sending FCM:", err);
    await snap.ref.update({
      sent: false,
      error: String(err)
    });
  }
});

