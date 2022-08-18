import { SerializedStyles } from '@emotion/react';

interface FieldsetCssPropType {
  div?: SerializedStyles;
  input?: SerializedStyles;
  label?: SerializedStyles;
}

interface InputRefType {
  [index: string]: React.RefObject<HTMLInputElement>;
}

interface ModalPosType {
  top?: number;
  right?: number;
  bottom?: number;
  left?: number;
}

export { FieldsetCssPropType, InputRefType, ModalPosType };
