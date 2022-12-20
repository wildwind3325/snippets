DirPath = "";
        Roots = new ArrayList<>();
        Roots.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        try {
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<?> sv = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = sm.getClass().getMethod("getVolumeList");
            Method getPath = sv.getMethod("getPath");
            Method isRemovable = sv.getMethod("isRemovable");
            Object result = getVolumeList.invoke(sm);
            int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object volume = Array.get(result, i);
                String path = (String) getPath.invoke(volume);
                boolean removable = (Boolean) isRemovable.invoke(volume);
                if (removable) {
                    Roots.add(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }