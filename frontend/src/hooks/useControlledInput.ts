import { useEffect, useState } from 'react';

function useControlledInput(initialInputValue?: string) {
  const [inputValue, setInputValue] = useState<string>(initialInputValue ?? '');

  const onChangeValue = ({
    target,
  }: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLSelectElement>) => {
    if (target instanceof HTMLInputElement || target instanceof HTMLSelectElement) {
      setInputValue(target.value);
    }
  };

  useEffect(() => {
    setInputValue(initialInputValue ?? '');
  }, [initialInputValue]);

  return { inputValue, setInputValue, onChangeValue };
}

export default useControlledInput;
