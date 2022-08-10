import { useTheme } from '@emotion/react';
import { forwardRef } from 'react';

import { FieldsetCssPropType } from '@/@types';

import { fieldsetStyle, inputStyle, labelStyle } from './Fieldset.styles';

interface FieldsetProps extends React.HTMLAttributes<HTMLInputElement> {
  type?: string;
  cssProp?: FieldsetCssPropType;
  labelText?: string;
  defaultValue?: string;
  autoFocus?: boolean;
  refProp?: React.MutableRefObject<null | HTMLInputElement>;
  disabled?: boolean;
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
        css={[inputStyle(theme), cssProp?.input]}
        placeholder={placeholder}
        defaultValue={defaultValue}
        autoFocus={autoFocus}
        ref={refProp}
        disabled={disabled}
      />
    </div>
  );
}

export default forwardRef(Fieldset);
