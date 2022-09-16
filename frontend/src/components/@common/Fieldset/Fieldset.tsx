import { useTheme } from '@emotion/react';

import { FieldsetCssPropType } from '@/@types';

import { errorMessageStyle, fieldsetStyle, inputStyle, labelStyle } from './Fieldset.styles';

interface FieldsetProps extends React.HTMLAttributes<HTMLInputElement> {
  type?: string;
  value?: string;
  defaultValue?: string;
  cssProp?: FieldsetCssPropType;
  labelText?: string;
  autoFocus?: boolean;
  refProp?: React.MutableRefObject<null | HTMLInputElement>;
  disabled?: boolean;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  isValid?: boolean;
  errorMessage?: string;
  min?: string | number;
  max?: string | number;
}

function Fieldset({
  type = 'text',
  id,
  cssProp,
  placeholder,
  value,
  defaultValue,
  autoFocus,
  refProp,
  disabled,
  onChange,
  labelText,
  isValid,
  errorMessage,
  min,
  max,
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
        value={value}
        defaultValue={defaultValue}
        autoFocus={autoFocus}
        ref={refProp}
        disabled={disabled}
        onChange={onChange}
        min={min}
        max={max}
      />
      {errorMessage && <span css={errorMessageStyle(theme, isValid)}>{errorMessage}</span>}
    </div>
  );
}

export default Fieldset;
