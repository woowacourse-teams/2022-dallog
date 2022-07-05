import '@emotion/react';

interface Colors {
  [key: string]: string;
}

declare module '@emotion/react' {
  export interface Theme {
    colors: Colors;
  }
}
