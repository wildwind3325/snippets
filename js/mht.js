var fs = require('fs');
var iconv = require('iconv-lite');

let dir = 'E:\\Download\\1\\';

let buf = fs.readFileSync('E:\\Download\\nati.mht');
let str = iconv.decode(buf, 'gb2312');
let data = str.split(/\r\n|\r|\n/);
let file = '', name = '';
for (let i = 0; i < data.length; i++) {
  let line = data[i];
  if (!file) {
    if (line.startsWith('Content-Type: image')) {
      let arr = data[i + 2].split('/');
      name = arr[arr.length - 1];
      file = data[i + 4];
      i = i + 4;
    } else {
      continue;
    }
  } else {
    if (line.length > 0) {
      file += line;
    } else {
      if (fs.existsSync(dir + name)) {
        console.log(name + '已存在');
        break;
      } else {
        fs.writeFileSync(dir + name, Buffer.from(file, 'base64'));
        file = '';
        name = '';
      }
    }
  }
}