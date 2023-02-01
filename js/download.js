// 字符串回传
// 后端方法将文件buffer对象处理成base64字符串，返回给前端。
res.send({
  file: Buffer.from(buffer).toString('base64')
});
// 前端将字符串转为Blob对象并触发浏览器下载
let str = atob(res.data.file);
let arr = new Uint8Array(str.length);
for (let i = 0; i < str.length; i++) arr[i] = str.charCodeAt(i);
// MIME类型根据实际需要填写
let url = URL.createObjectURL(new Blob([arr], {type: 'application/vnd.ms-excel'}));
// 创建一个游离的a对象并触发其点击事件
let hlk = document.createElement('a');
hlk.href = url;
hlk.download = 'export.xlsx';
hlk.click();
hlk.remove();
URL.revokeObjectURL(url);

// 兼容性中等，支持绝大部分现代浏览器，可能出现某些国产浏览器不能响应，该方式不能阻止浏览器自动打开文件。

// 一般文件下载
res.download('D:/report-12345.pdf');
res.sendFile('D:/report-12345.pdf');
res.send(buffer); // 不能阻止自动打开

// 设置响应头
res.attachment('D:/to/logo.png'); // 告诉浏览器强制下载附件

// 将文件buffer转为前端下载最佳方法如下
res.status(200).set({
      'Content-Type': 'image/png',
      'Content-Disposition': 'attachment; filename=logo.png'
    });
res.end(buffer);