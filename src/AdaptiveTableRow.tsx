import { UIManager, findNodeHandle } from 'react-native';
import React, { useEffect, useRef, useState } from 'react';

export function AdaptiveTableRow({ index, onReady }) {
  const ref = useRef(null);
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    const measureAndSend = () => {
      if (!ref.current) return;

      UIManager.measure(findNodeHandle(ref.current), (x, y, width, height) => {
        if (width > 0 && height > 0) {
          onReady(ref.current, index); // Send ref and index to native
          setIsReady(true);
        } else {
          // Retry if layout isn't done yet
          setTimeout(measureAndSend, 50);
        }
      });
    };

    measureAndSend();
  }, []);

  return (
    <View
      ref={ref}
      collapsable={false}
      style={{
        width: 300,
        height: 150,
        backgroundColor: index % 2 === 0 ? 'lightgray' : 'white',
        padding: 16,
        borderBottomWidth: 1,
        borderBottomColor: '#ccc',
      }}
    >
      <Text>Row {index + 1}</Text>
    </View>
  );
}
