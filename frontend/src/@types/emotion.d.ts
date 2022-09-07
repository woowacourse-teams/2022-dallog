import { SerializedStyles } from '@emotion/react';

interface Colors {
  [key: string]: string;
}

type Flex = Record<'row' | 'column', SerializedStyles>;

declare module '@emotion/react' {
  export interface Theme {
    colors: Colors;
    flex: Flex;
  }
}
