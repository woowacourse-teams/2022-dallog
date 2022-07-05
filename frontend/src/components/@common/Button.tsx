import { button } from './Button.styles';

interface ButtonProps {
  type?: 'button' | 'submit' | 'reset';
  onClick: () => void;
  children?: string | JSX.Element | JSX.Element[];
}

function Button({ type = 'button', onClick, children, ...props }: ButtonProps) {
  return (
    <button type={type} css={button} onClick={onClick} {...props}>
      {children}
    </button>
  );
}

export default Button;
