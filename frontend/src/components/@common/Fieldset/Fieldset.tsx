import { useTheme } from '@emotion/react';

import { FieldsetCssPropType } from '@/@types';

import { errorMessageStyle, fieldsetStyle, inputStyle, labelStyle } from './Fieldset.styles';

interface FieldsetProps extends React.HTMLAttributes<HTMLInputElement> {
  type?: string;
  cssProp?: FieldsetCssPropType;
  labelText?: string;
  defaultValue?: string;
  autoFocus?: boolean;
  refProp?: React.MutableRefObject<null | HTMLInputElement>;
  disabled?: boolean;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  errorMessage?: string;
  isValid?: boolean;
}

function Fieldset({
  type = 'text',
  id,
  cssProp,
  labelText,
  placeholder,
  defaultValue,
  autoFocus,
  refProp,
  disabled,
  onChange,
  errorMessage,
  isValid,
}: FieldsetProps) {
  const theme = useTheme();

  return (
    <div css={[fieldsetStyle(theme), cssProp?.div]}>
      {labelText && (
        <label htmlFor={id} css={[labelStyle, cssProp?.label]}>
          {labelText}
        </label>
      )}
      <input
        type={type}
        id={id}
        css={[inputStyle(theme, isValid), cssProp?.input]}
        placeholder={placeholder}
        defaultValue={defaultValue}
        autoFocus={autoFocus}
        ref={refProp}
        disabled={disabled}
        onChange={onChange}
      />
      <span css={errorMessageStyle(theme, isValid)}>{errorMessage}</span>
    </div>
  );
}

export default Fieldset;
