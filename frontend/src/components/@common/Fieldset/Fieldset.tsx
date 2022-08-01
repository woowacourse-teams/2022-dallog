import { useTheme } from '@emotion/react';
import { forwardRef } from 'react';

import { FieldsetCssPropType } from '@/@types';

import { fieldset, input, label } from './Fieldset.styles';

interface FieldsetProps extends React.HTMLAttributes<HTMLInputElement> {
  type?: string;
  cssProp?: FieldsetCssPropType;
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
    <div css={[fieldset(theme), cssProp?.div]}>
      {labelText && (
        <label htmlFor={id} css={[label, cssProp?.label]}>
          {labelText}
        </label>
      )}
      <input
        type={type}
        id={id}
        css={[input(theme), cssProp?.input]}
        placeholder={placeholder}
        defaultValue={defaultValue}
        autoFocus={autoFocus}
        ref={refProp}
      />
    </div>
  );
}

export default forwardRef(Fieldset);
