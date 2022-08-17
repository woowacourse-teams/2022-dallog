import { css, Theme } from '@emotion/react';

const buttonStyle = ({ colors }: Theme) => css`
  position: relative;

  width: 8rem;
  height: 8rem;

  background: transparent;

  color: ${colors.GRAY_700};

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }

  &:hover span {
    visibility: visible;
  }
`;

const menuTitle = ({ colors }: Theme) => css`
  visibility: hidden;
  position: absolute;
  top: 120%;
  left: 50%;
  transform: translateX(-50%);

  padding: 2rem 3rem;

  background: ${colors.GRAY_700}ee;

  font-size: 3rem;
  font-weight: normal;
  color: ${colors.WHITE};
  white-space: nowrap;
`;

const categoryItemStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-around;

  height: 20rem;
  border-bottom: 1px solid ${colors.GRAY_400};

  font-size: 4rem;
`;

const itemStyle = css`
  flex: 1 1 0;
  text-align: center;
`;

export { buttonStyle, categoryItemStyle, itemStyle, menuTitle };
