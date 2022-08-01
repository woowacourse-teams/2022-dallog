import { SerializedStyles, useTheme } from '@emotion/react';
import { forwardRef } from 'react';

import { fieldset, input, label } from './Fieldset.styles';

interface FieldsetProps extends React.HTMLAttributes<HTMLInputElement> {
  type?: string;
  cssProp?: SerializedStyles;
  labelText?: string;
  defaultValue?: string;
  autoFocus?: boolean;
  refProp?: React.MutableRefObject<null | HTMLInputElement>;
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
}: FieldsetProps) {
  const theme = useTheme();

  return (
    <div css={[fieldset(theme), cssProp]}>
      {labelText && (
        <label htmlFor={id} css={label}>
          {labelText}
        </label>
      )}
      <input
        type={type}
        id={id}
        css={input(theme)}
        placeholder={placeholder}
        defaultValue={defaultValue}
        autoFocus={autoFocus}
        ref={refProp}
      />
    </div>
  );
}

export default forwardRef(Fieldset);
