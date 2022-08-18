import { SerializedStyles, Theme } from '@emotion/react';

import { button } from './Button.styles';

interface ButtonProps {
  type?: 'button' | 'submit' | 'reset';
  cssProp?: SerializedStyles | (({ colors, flex }: Theme) => SerializedStyles);
  onClick?: (e?: React.FormEvent) => void;
  children?: string | JSX.Element | JSX.Element[];
  disabled?: boolean;
}

function Button({
  type = 'button',
  cssProp,
  onClick,
  children,
  disabled = false,
  ...props
}: ButtonProps) {
  return (
    <button {...props} type={type} css={[button, cssProp]} onClick={onClick} disabled={disabled}>
      {children}
    </button>
  );
}

export default Button;
