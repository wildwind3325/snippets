public void setIcon() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification n = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notify)
                .setTicker("Ticker")
                .setContentTitle("内容标题")
                .setContentText("内容文本")
                .setContentIntent(pi).setNumber(1).getNotification();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(Notification.FLAG_SHOW_LIGHTS, n);
    }