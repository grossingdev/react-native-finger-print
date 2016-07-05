/**
 * Created by BaeBae on 7/4/16.
 */

/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  TouchableOpacity,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import FingerPrint from 'react-native-fingerprint';

export default class FingerPrintAuth extends Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      intro: false,
      error: '',
      success: false
    }
    this.onFingerAuth = this.onFingerAuth.bind(this);
    this.onRetry = this.onRetry.bind(this);
  }
  componentDidMount() {
    FingerPrint.registerFingerPrintCallback(this.onFingerAuth);
    FingerPrint.initializeFingerPrintAuth();
  }

  componentWillUnmount() {
    FingerPrint.removeFingerPrintCallback();
  }

  onRetry() {
    FingerPrint.initializeFingerPrintAuth(this.onFingerAuth);
    this.setState({ intro: false, success: false });
  }

  onFingerAuth(result) {
    if (result.success) {
      this.setState({ intro: true, success: true });
    } else {
      this.setState( { intro: true, success: false, error: result.error });
    }
    console.info('result', result);
  }

  renderAuthStatus() {
    const { success, error } = this.state;
    if (success) {
      return (
        <View>
          <Text style={styles.instructions}>Finger Auth Succeed</Text>
          <TouchableOpacity onPress={this.onRetry} style={styles.btnContainer}>
            <Text>Retry Auth again.</Text>
          </TouchableOpacity>
        </View>
      )
    }
    return (
      <Text style={styles.instructions}>{error}</Text>
    )
  }
  render() {
    return (
      <View style={styles.container}>
        { !this.state.intro &&
          <Text style={styles.welcome}>
            Please tap Home button to Finger Auth
          </Text>
        }

        {this.renderAuthStatus()}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  btnContainer: {
    width: 150,
    height: 40,
    backgroundColor: 'gray',
    alignItems: 'center',
    justifyContent: 'center'
  }
});

