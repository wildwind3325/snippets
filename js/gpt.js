var http = require('./http');

var chat = async () => {
  let res = await http.request({
    method: 'POST',
    url: 'https://api.openai.com/v1/chat/completions',
    headers: {
      'Authorization': 'Bearer sk-'
    },
    body: {
      model: 'gpt-3.5-turbo',
      messages: [{
        role: 'user',
        content: '如何看待政治正确这一现象？'
      }]
    },
    json: true,
    proxy: 'http://127.0.0.1:10809'
  });
  console.log(res.body.choices[0].message.content.replace(/\n/g, ''));
};

var image = async () => {
  let res = await http.request({
    method: 'POST',
    url: 'https://api.openai.com/v1/images/generations',
    headers: {
      'Authorization': 'Bearer sk-'
    },
    body: {
      prompt: '小狗在嬉戏',
      size: '512x512'
    },
    json: true,
    proxy: 'http://127.0.0.1:10809'
  });
  console.log(res.body.data[0].url);
};

(async () => {
  await chat();
})();
