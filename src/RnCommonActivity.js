import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  NativeModules,
  TouchableNativeFeedback,
  ToastAndroid
} from 'react-native';
import HelloWorld from './HelloWorld';
import TestView from './TestView';
import GetAllNativeModules from './GetAllNativeModules';

class RnCommonActivity extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      test: 'RnCommonActivity'
    };
  }

  render() {
    switch (this.props.COMPONENT_TYPE) {
      case 'HelloWorld':
        return <HelloWorld COMPONENT_TYPE={this.props.COMPONENT_TYPE}/>;
      case 'TestView':
        return <TestView COMPONENT_TYPE={this.props.COMPONENT_TYPE}/>;
      case 'GetAllNativeModules':
        return <GetAllNativeModules COMPONENT_TYPE={this.props.COMPONENT_TYPE}/>;
      default:
        return (
          <View>
            <Text>{this.props.COMPONENT_TYPE}</Text>
          </View>
        );
    }
  }
}

export default RnCommonActivity;
