import { useEffect, useState } from 'react';

function useControlledInput(defaultInputValue?: string) {
  const [inputValue, setInputValue] = useState<string>(defaultInputValue ?? '');

  const onChange = (e: KeyboardEvent) => {
    if (e.target instanceof HTMLInputElement) {
      setInputValue(e.target.value);
    }
  };

  useEffect(() => {
    setInputValue(defaultInputValue ?? '');
  }, [defaultInputValue]);

  return { inputValue, onChange };
}

export default useControlledInput;
