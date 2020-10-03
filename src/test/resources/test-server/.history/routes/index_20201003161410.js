var express = require('express');
const path = require('path');
const multer = require('multer');
const gUpload = multer({
  dest: path.join(global.__base, '/upload')
});

var router = express.Router();

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/get', function (req, res, next) {
  let data = {};
  data.headers = req.headers;
  data.param = req.query;
  res.send(data);
});

router.post('/post', function (req, res, next) {
  let data = {};
  data.headers = req.headers;
  data.body = req.body;
  res.send(data);
});

router.get('/result', function (req, res, next) {
  let data = {};
  data.data = req.query.data;
  data.success = req.query.success;
  res.send(data);
});

router.post('/resultList', function (req, res, next) {
  let data = {};
  data.data = req.body;
  data.success = true;
  res.send(data);
});

router.post('/resultListObject', function (req, res, next) {
  let data = {};
  data.data = req.body;
  data.success = true;
  res.send(data);
});

router.get('/timeout', function (req, res, next) {
  let data = {};
  data.headers = req.headers;
  data.body = req.body;
  setTimeout(function () {
    res.send(data);
  }, 4000);
});

router.get('/user/:id', function (req, res, next) {
  let data = {};
  data.headers = req.headers;
  data.userId = req.params.id;
  res.send(data);
});

router.post('/upload', gUpload.any(), function (req, res, next) {
  if (!req.files ||
    req.files.length === 0) {
    res.send('no file');
    return;
  }
  // req.files.forEach(file => {
  //   console.log('filename:' + file.filename);
  // });
  res.send(req.files);
});

module.exports = router;
