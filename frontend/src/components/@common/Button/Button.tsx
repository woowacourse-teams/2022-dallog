import { SerializedStyles } from '@emotion/react';

import { button } from './Button.styles';

interface ButtonProps {
  type?: 'button' | 'submit' | 'reset';
  css?: SerializedStyles;
  onClick?: () => void;
  children?: string | JSX.Element | JSX.Element[];
}

function Button({ type = 'button', css, onClick, children, ...props }: ButtonProps) {
  return (
    <button type={type} css={[button, css]} onClick={onClick} {...props}>
      {children}
    </button>
  );
}

export default Button;
