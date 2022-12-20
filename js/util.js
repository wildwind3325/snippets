if (/mobile|Android|iPhone|MicroMessenger/i.test(navigator.userAgent)) {
  router = router_mobile;
} else {
  router = router_desktop;
}