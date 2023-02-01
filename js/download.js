// �ַ����ش�
// ��˷������ļ�buffer�������base64�ַ��������ظ�ǰ�ˡ�
res.send({
  file: Buffer.from(buffer).toString('base64')
});
// ǰ�˽��ַ���תΪBlob���󲢴������������
let str = atob(res.data.file);
let arr = new Uint8Array(str.length);
for (let i = 0; i < str.length; i++) arr[i] = str.charCodeAt(i);
// MIME���͸���ʵ����Ҫ��д
let url = URL.createObjectURL(new Blob([arr], {type: 'application/vnd.ms-excel'}));
// ����һ�������a���󲢴��������¼�
let hlk = document.createElement('a');
hlk.href = url;
hlk.download = 'export.xlsx';
hlk.click();
hlk.remove();
URL.revokeObjectURL(url);

// �������еȣ�֧�־��󲿷��ִ�����������ܳ���ĳЩ���������������Ӧ���÷�ʽ������ֹ������Զ����ļ���

// һ���ļ�����
res.download('D:/report-12345.pdf');
res.sendFile('D:/report-12345.pdf');
res.send(buffer); // ������ֹ�Զ���

// ������Ӧͷ
res.attachment('D:/to/logo.png'); // ���������ǿ�����ظ���

// ���ļ�bufferתΪǰ��������ѷ�������
res.status(200).set({
      'Content-Type': 'image/png',
      'Content-Disposition': 'attachment; filename=logo.png'
    });
res.end(buffer);