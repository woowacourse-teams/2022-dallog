import { useState } from 'react';

function useToggle(initialState = false) {
  const [state, setState] = useState(initialState);

  const toggleState = () => {
    setState((prev) => !prev);
  };

  return { state, toggleState };
}

export default useToggle;
