import React, { useRef } from 'react';
import { useEffect, useMemo, useState } from 'react';
import { View, StyleSheet, Text, InteractionManager } from 'react-native';
import {
  CurrencyInput,
  AdaptivePagerView,
  AdaptiveTableView,
} from 'react-native-my-react-native-package1';
import TableScreen from '../../src/adaptiveTableView1';
import AdaptiveTableView1 from '../../src/adaptiveTableView1';

export default function App() {
  const [ttt, setT] = useState('1000');
  const [rowIndices, setRowIndices] = useState([]);

  useEffect(() => {
    InteractionManager.runAfterInteractions(() => {
      setRowIndices(Array.from({ length: 50 }, (_, i) => i));
    });
  }, []);

  const Row = React.memo(({ index }) => {
    const height = 200;
    const rowRef = useRef();

    useEffect(() => {
      if (rowRef.current) {
        rowRef.current.setNativeProps({
          // nativeID: `row-height-${index}-${height}`,
          height: height,
          width: 200,
        });
      }
    }, [height]);

    return (
      <View
        collapsable={false}
        ref={rowRef}
        style={{
          width: 200,
          height: 200,
          backgroundColor: index % 2 === 0 ? '#f5f5f5' : 'white',
          padding: 16,
          borderBottomWidth: 1,
          borderBottomColor: '#ddd',
        }}
      >
        <Text
          style={{ fontSize: 16, width: 100, height: 100 }}
        >{`Row ${index + 1}`}</Text>
        {/* <FastImage
          source={{
            uri: 'https://www.bigfootdigital.co.uk/wp-content/uploads/2020/07/image-optimisation-scaled.jpg',
          }}
          style={{ width: 300, height: height - 70, marginTop: 12 }}
          resizeMode="cover"
        /> */}
      </View>
    );
  });

  const rows = useMemo(
    () => rowIndices.map((i) => <Row key={`row-${i}`} index={i} />),
    [rowIndices]
  );

  const onChangeText = (event: { nativeEvent: { text: string } }) => {
    setT(event.nativeEvent.text);
    console.log('text is ', ttt);
  };
  return (
    <View style={styles.container}>
      <AdaptiveTableView1
        style={{ flex: 1 }}
        onEndReached={() => {
          console.log('Load more triggered!');
          // Load more data here
        }}
      >
        {Array.from({ length: 10 }).map((_, index) => (
          <View
            key={index}
            style={{
              width: 'auto',
              height: 100 + index * 10,
              backgroundColor: index % 2 === 0 ? '#eee' : '#ccc',
              margin: 10,
            }}
          >
            <Text>{`Row ${index + 1}`}</Text>
          </View>
        ))}
      </AdaptiveTableView1>
      {/* <AdaptiveTableView
        style={{ height: '100%' }}
        // onRowSelected={(event) => setSelectedRow(event.nativeEvent.index)}
        // onEndReached={debouncedLoadMore}
      >
        {rows}
      </AdaptiveTableView> */}
      {/* <CurrencyInput
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
      /> */}
      {/* <AdaptivePagerView
        style={{ height: 200 }}
        // itemSize={{ width: 250, height: 150 }}
        // onPageSelected={(event) => {
        //   console.log('Selected page:', event.nativeEvent.position);
        // }}
      >
        <View style={{ backgroundColor: 'red', height: 150, width: 250 }} />
        <View style={{ backgroundColor: 'blue', height: 150, width: 250 }} />
        <View style={{ backgroundColor: 'green', height: 150, width: 250 }} />
      </AdaptivePagerView> */}
      {/* <Text>{ttt}</Text>
      <AdaptivePagerView
        style={{ height: 200, width: 400 }}
        itemSize={{ width: 250, height: 150 }}
        itemSpacing={12}
        onPageSelected={(event) => {
          setT(event.nativeEvent.position);
        }}
      >
        <View
          collapsable={false}
          style={{
            backgroundColor: 'red',
            width: 250,
            height: 150,
            flexDirection: 'row',
          }}
        >
          <Text style={{ color: 'blue' }}>Page 1 </Text>
          <Text>Page 2 </Text>
        </View>
        <View
          collapsable={false}
          style={{ backgroundColor: 'blue', width: 250, height: 150 }}
        />
        <View
          collapsable={false}
          style={{ backgroundColor: 'green', width: 250, height: 150 }}
        >
          <Text style={{ color: 'blue' }}>Page 1 </Text>
          <Text>Page 2 </Text>
        </View>
      </AdaptivePagerView> */}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
