var http = require('http');
var bodyParser = require("body-parser");
var express = require('express');
var path = require('path');

var app = express();


app.set('port', process.env.PORT || 8080);
//app.use(express.static(__dirname));
app.listen(app.get('port'));

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.static(path.join(__dirname, 'bower_components')));

console.log('App Server is listening on port ' + app.get('port'));

app.use(bodyParser.urlencoded({extended: false}));
/*
app.get('/', function (req, res)
{
    res.sendfile("public/index.html");
});

app.get('/test', function (req, res)
{
    res.sendfile("public/test.html");
});

app.post('/login', function (req, res)
{
    var user_name = req.body.username;
    console.log("User name = " + user_name + ", password is ");


    var username = "drewmutt";
    var postheaders = {
        'Content-Type' : 'application/json',
        'Content-Length' : Buffer.byteLength(username, 'utf8')
    };

    var optionsget = {
        host : '192.168.2.2', // here only the domain name
        // (no http/https !)
        port : 8080,
        path : '/aerial/getplaylists', // the rest of the url with parameters if needed
        method : 'POST',
        headers: postheaders
    };

    console.info('Options prepared:');
    console.info(optionsget);
    console.info('Do the GET call');

    var reqGet = http.request(optionsget, function(res1) {
        console.log("statusCode: ", res.statusCode);
        // uncomment it for header details
//  console.log("headers: ", res.headers);


        res1.on('data', function(d) {
            console.info('GET result:\n');
            process.stdout.write(d);
            console.info('\n\nCall completed');
            res.end(d);
        });


    });

    reqGet.write(username);
    reqGet.end();



});

*/