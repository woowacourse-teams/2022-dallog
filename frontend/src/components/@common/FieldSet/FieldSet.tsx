import { fieldSet, input, label } from './FieldSet.styles';

interface FieldSetProps extends HTMLInputElement {
  labelText?: string;
}

function FieldSet({ type = 'text', id, labelText, placeholder }: FieldSetProps) {
  return (
    <div css={fieldSet}>
      <label htmlFor={id} css={label}>
        {labelText}
      </label>
      <input type={type} id={id} css={input} placeholder={placeholder} />
    </div>
  );
}

export default FieldSet;
