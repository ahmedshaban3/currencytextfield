import { useState } from 'react';
import { View, StyleSheet } from 'react-native';
import { MyReactNativePackage1View } from 'react-native-my-react-native-package1';

export default function App() {
  const [ttt, setT] = useState('1000');

  const onChangeText = (event: { nativeEvent: { text: string } }) => {
    setT(event.nativeEvent.text);
    console.log('text is ', ttt);
  };
  return (
    <View style={styles.container}>
      <MyReactNativePackage1View
        key={ttt}
        text={ttt}
        // eslint-disable-next-line react-native/no-inline-styles
        style={{
          alignItems: 'center',
          width: '100%',
          height: 120,
        }}
        onChangeText={onChangeText}
        fontColor={'#000000'}
        fontSize={39}
        placeholderColor={'#9A9A9A'}
        currency={'SAR'}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
