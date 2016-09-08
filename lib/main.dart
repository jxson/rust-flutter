import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(
    new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new FlutterDemo(),
    ),
  );
}

class FlutterDemo extends StatefulWidget {
  FlutterDemo({Key key}) : super(key: key);

  @override
  _FlutterDemoState createState() => new _FlutterDemoState();
}

class _FlutterDemoState extends State<FlutterDemo> {
  int _counter = 0;
  String _message = '';

  void _incrementCounter() {
    print('== Flutter ==> _incrementCounter() called');
    setState(() {
      _counter++;
      _hello();
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Flutter Demo'),
      ),
      body: new Center(
        child: new Text(
          'Button tapped $_counter time${ _counter == 1 ? '' : 's' }. '
          '==> ${ _message } <=='
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: new Icon(Icons.add),
      ),
    );
  }

  Future<Null> _hello() async {
    print('== Flutter ==> sending message');

    final Map<String, String> message = <String, String>{'value': null};
    final Map<String, String> reply = await HostMessages.sendJSON('hello', message);

    print('== Flutter ==> received reply');

    if (! mounted) return;

    setState(() {
      _message = reply['value'];
    });
  }
}
