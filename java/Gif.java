AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start("/mnt/sdcard/Images/new.gif");
        encoder.setDelay(1000);
        encoder.setRepeat(0);
        Bitmap img1 = BitmapFactory.decodeFile("/mnt/sdcard/Images/011.jpg");
        Bitmap img2 = BitmapFactory.decodeFile("/mnt/sdcard/Images/012.jpg");
        encoder.addFrame(img1);
        encoder.addFrame(img2);
        encoder.finish();