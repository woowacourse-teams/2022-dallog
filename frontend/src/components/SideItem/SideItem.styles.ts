import { css, Theme } from '@emotion/react';

const itemStyle = ({ colors, flex }: Theme) => css`
  ${flex.row};

  justify-content: space-between;

  width: 100%;
  height: 8rem;

  &:hover {
    background-color: ${colors.GRAY_100};

    button {
      visibility: visible;
    }
  }
`;

const checkBoxNameStyle = ({ flex }: Theme) => css`
  ${flex.row};

  gap: 1rem;

  &:hover {
    cursor: pointer;
  }
`;

const nameStyle = css`
  overflow: hidden;
  position: relative;

  width: 32rem;

  white-space: nowrap;
  text-overflow: ellipsis;
`;

const headerStyle = css`
  padding: 2rem;

  font-size: 5rem;
`;

const iconStyle = css`
  visibility: hidden;
`;

const modalLayoutStyle = css`
  position: relative;
`;

export { itemStyle, checkBoxNameStyle, headerStyle, iconStyle, nameStyle, modalLayoutStyle };
