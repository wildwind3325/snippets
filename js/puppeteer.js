var puppeteer = require('puppeteer-core');

(async () => {
  const browser = await puppeteer.launch({
    headless: false,
    executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe',
    defaultViewport: null,
    // args: ['-app=https://www.baidu.com'],
    args: [
      '--proxy-server=127.0.0.1:10809',
      '--proxy-bypass-list=<-loopback>',
    ],
    ignoreDefaultArgs: ['--enable-automation']
  });
  let pages = await browser.pages();
  let page = pages[0];
  await page.goto('https://accounts.pixiv.net/login?lang=zh&source=accounts&view_type=page');
  await page.evaluate(() => {
    let account = document.querySelector('input[type="text"]');
    account.value = '1';
    let password = document.querySelector('input[type="password"]');
    password.value = '1';
    window.setTimeout(() => {
      document.querySelectorAll('button[type="submit"]')[4].click();
    }, 1000);
  });
  // let elms = await page.$$('button[type="submit"]');
  // let button = elms[4];
  // await button.click();
  // await page.exposeFunction('uuid', () => '12345');
  // await page.keyboard.press('Enter');
})();