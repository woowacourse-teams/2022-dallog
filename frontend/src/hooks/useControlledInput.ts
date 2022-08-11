import { useState } from 'react';

function useControlledInput() {
  const [inputValue, setInputValue] = useState('');

  const onChange = (e: KeyboardEvent) => {
    if (e.target instanceof HTMLInputElement) {
      setInputValue(e.target.value);
    }
  };

  return { inputValue, onChange };
}

export default useControlledInput;
