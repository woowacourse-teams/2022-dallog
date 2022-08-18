import { SerializedStyles } from '@emotion/react';

interface Colors {
  [key: string]: string;
}

interface Flex {
  [key: string]: SerializedStyles;
}

declare module '@emotion/react' {
  export interface Theme {
    colors: Colors;
    flex: Flex;
  }
}
