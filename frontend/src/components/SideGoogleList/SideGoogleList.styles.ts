import { css, Theme } from '@emotion/react';

const listStyle = ({ flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.column}

  display: ${isSideBarOpen ? 'flex' : 'none'};
  justify-content: flex-start;

  width: 54rem;

  font-size: 4rem;
`;

const headerLayoutStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
`;

const headerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  height: 8rem;

  font-weight: bold;

  cursor: pointer;
`;

const contentStyle = ({ flex }: Theme, isListOpen: boolean, listLength: number) => css`
  ${flex.column};

  gap: 2rem;
  overflow: hidden;

  width: 100%;
  height: ${isListOpen ? `${9 * listLength}rem` : 0};
  margin-bottom: 5rem;

  transition: height 0.3s ease-in-out;
`;

const menuStyle = ({ colors }: Theme) => css`
  position: relative;

  width: 9rem;
  height: 9rem;

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }

  &:hover span {
    visibility: visible;
  }
`;

const menuTitleStyle = ({ colors }: Theme) => css`
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

export { contentStyle, headerLayoutStyle, headerStyle, listStyle, menuStyle, menuTitleStyle };
