    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF8"));
            return bytesToHexStr(md.digest()).toUpperCase();
        } catch (Exception ex) {
            return "";
        }
    }
    
    private static char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String bytesToHexStr(byte[] bytes) {
        StringBuilder s = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            s.append(bcdLookup[(bytes[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bytes[i] & 0x0f]);
        }
        return s.toString();
    }

    public static byte[] hexStrToBytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }