import { useEffect, useState } from 'react';

function useControlledInput(defaultInputValue?: string) {
  const [inputValue, setInputValue] = useState<string>(defaultInputValue ?? '');

  const onChangeValue = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target instanceof HTMLInputElement) {
      setInputValue(e.target.value);
    }
  };

  useEffect(() => {
    setInputValue(defaultInputValue ?? '');
  }, [defaultInputValue]);

  return { inputValue, onChangeValue };
}

export default useControlledInput;
