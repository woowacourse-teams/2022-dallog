import { SerializedStyles, useTheme } from '@emotion/react';
import { forwardRef } from 'react';

import { fieldSet, input, label } from './FieldSet.styles';

interface FieldSetProps extends React.HTMLAttributes<HTMLInputElement> {
  type?: string;
  cssProp?: SerializedStyles;
  labelText?: string;
  defaultValue?: string;
  autoFocus?: boolean;
  refProp?: React.MutableRefObject<null | HTMLInputElement>;
}

function FieldSet({
  type = 'text',
  id,
  cssProp,
  labelText,
  placeholder,
  defaultValue,
  autoFocus,
  refProp,
}: FieldSetProps) {
  const theme = useTheme();

  return (
    <div css={[fieldSet(theme), cssProp]}>
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

export default forwardRef(FieldSet);
