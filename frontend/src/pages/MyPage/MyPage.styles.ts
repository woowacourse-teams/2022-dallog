import { css, Theme } from '@emotion/react';

const myPage = ({ colors }: Theme) => css`
  width: 100%;
  height: 100%;
  padding: 2rem;

  background: ${colors.GRAY_100};
`;

const infoTable = ({ colors }: Theme) => css`
  width: 60%;
  padding: 4rem;
  margin: 0 auto;
  border: 1px solid ${colors.GRAY_500};

  background: ${colors.WHITE};
`;

const infoTableHeader = ({ colors }: Theme) => css`
  width: 100%;
  margin: 0 auto;
  padding: 4rem;
  border-bottom: 1px solid ${colors.GRAY_500};

  font-size: 5rem;
`;

const textInfo = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 96%;
  padding: 3rem;
  margin: 5rem auto;
  border-bottom: 1px solid ${colors.GRAY_500};

  font-size: 3.5rem;
`;

const imageInfo = ({ flex }: Theme) => css`
  ${flex.row}

  align-items: flex-start;
  justify-content: space-between;

  width: 96%;
  height: 40rem;
  padding: 3rem;
  margin: 5rem auto;

  font-size: 3.5rem;
`;

const imageSize = css`
  width: 40rem;
  height: 40rem;
`;

export { myPage, infoTable, infoTableHeader, textInfo, imageInfo, imageSize };
