import { SerializedStyles, Theme } from '@emotion/react';

import { button } from './Button.styles';

interface ButtonProps {
  type?: 'button' | 'submit' | 'reset';
  cssProp?: SerializedStyles | (({ colors, flex }: Theme) => SerializedStyles);
  onClick?: (e?: React.FormEvent) => void;
  children?: string | JSX.Element | JSX.Element[];
}

function Button({ type = 'button', cssProp, onClick, children, ...props }: ButtonProps) {
  return (
    <button {...props} type={type} css={[button, cssProp]} onClick={onClick}>
      {children}
    </button>
  );
}

export default Button;
