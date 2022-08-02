import { SerializedStyles } from '@emotion/react';

interface FieldsetCssPropType {
  div?: SerializedStyles;
  input?: SerializedStyles;
  label?: SerializedStyles;
}

interface InputRefType {
  [index: string]: React.RefObject<HTMLInputElement>;
}

export { FieldsetCssPropType, InputRefType };
