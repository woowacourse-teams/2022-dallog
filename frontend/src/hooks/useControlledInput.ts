import { useEffect, useState } from 'react';

function useControlledInput(initialInputValue?: string) {
  const [inputValue, setInputValue] = useState<string>(initialInputValue ?? '');

  const onChangeValue = ({ target }: React.ChangeEvent<HTMLInputElement>) => {
    if (target instanceof HTMLInputElement) {
      setInputValue(target.value);
    }
  };

  useEffect(() => {
    setInputValue(initialInputValue ?? '');
  }, [initialInputValue]);

  return { inputValue, setInputValue, onChangeValue };
}

export default useControlledInput;
