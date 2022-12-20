if (window.Notification) {
        if (Notification.permission === 'granted') {
            var notification = new Notification('Hello Notification', {
                body: '11',
                icon: 'https://www.baidu.com/img/bdlogo.png'
            });
        } else {
            Notification.requestPermission();
        }
    }