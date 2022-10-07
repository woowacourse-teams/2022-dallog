import { css, Theme } from '@emotion/react';

const categoryItem = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-around;

  height: 20rem;
  border-bottom: 1px solid ${colors.GRAY_400};

  font-size: 4rem;

  &:hover {
    background: ${colors.GRAY_100};

    cursor: pointer;
  }
`;

const item = css`
  flex: 1 1 0;
  text-align: center;
`;

const unsubscribeButton = ({ colors }: Theme) => css`
  position: relative;

  width: 15rem;
  height: 8rem;
  border-radius: 3px;

  background-color: ${colors.GRAY_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.WHITE};

  &:hover {
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

export { categoryItem, item, menuTitle, unsubscribeButton };
