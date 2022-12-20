

/**
 * 条形码相关
 */
class BarcodeUtil {
  /**
   * 构造新的实例
   */
  constructor() {
    this.canvas = document.createElement('canvas');
    this.codes = new Array();
    this.repeats = new Array();
    for (let i = 0; i < 19; i++) {
      this.codes.push(String.fromCharCode(71 + i));
      this.repeats.push(i + 1);
    }
    for (let i = 0; i < 20; i++) {
      this.codes.push(String.fromCharCode(103 + i));
      this.repeats.push(20 * (i + 1));
    }
  }

  /**
   * 将文本转换为点阵
   * @param {string} str 内容
   * @param {any} options 配置项
   */
  convert(str, options) {
    options = options || {};
    let fontSize = options.fontSize || 12;
    let fontFamily = options.fontFamily || 'SimSun';
    let font = fontSize + 'px ' + fontFamily;
    if (options.fontStyle) {
      font = options.fontStyle + ' ' + font;
    }
    let x = options.x || 0;
    let y = options.y || 0;
    let zoomX = options.zoomX || 1;
    let zoomY = options.zoomY || 1;
    let g = this.canvas.getContext('2d');
    g.font = font;
    let len = g.measureText(str).width + 4 + 0.35 * str.length;
    let width = parseInt(len / 8) * 8;
    if (len % 8 !== 0) {
      width += 8;
    }
    let height = fontSize + 2;
    this.canvas.width = width;
    this.canvas.height = height;
    g.fillStyle = '#ffffff';
    g.fillRect(0, 0, width, height);
    g.font = font;
    g.fillStyle = '#000000';
    g.textBaseline = 'top';
    g.fillText(str, 2, 0);
    let data = g.getImageData(0, 0, width, height).data;
    let content = '';
    for (let i = 0; i < width * height / 4; i++) {
      let value = 0;
      for (let j = 0; j < 4; j++) {
        if (data[i * 16 + j * 4 + 2] < 150) {
          value += parseInt(Math.pow(2, 3 - j));
        }
      }
      content += value.toString(16).toUpperCase();
    }
    let result = '~DGOUTSTR01,' + width * height / 8 + ',' + width / 8 + ',';
    let count = 1;
    for (let i = 1; i < content.length; i++) {
      if (content[i] === content[i - 1]) {
        count++;
        if (i == content.length - 1) {
          result += this.compress(count, true) + content[i];
        }
      }
      else {
        result += this.compress(count, true) + content[i - 1];
        count = 1;
      }
    }
    result += '\r\n^FO' + x + ',' + y + '^XGOUTSTR01,' + zoomX + ',' + zoomY + ',^FS';
    return result;
  }

  /**
   * 压缩路程
   * @param {number} len 长度
   */
  compress(len, original) {
    if (len === 1 && !original) return this.codes[0];
    if (len <= 1) {
      return '';
    }
    else {
      for (let i = this.repeats.length - 1; i >= 0; i--) {
        if (len >= this.repeats[i]) {
          return this.codes[i] + this.compress(len - this.repeats[i], false);
        }
      }
      return '';
    }
  }
}
export default BarCodeUtil;

