import { css, Theme } from '@emotion/react';

const listStyle = ({ flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.column}

  display: ${isSideBarOpen ? 'flex' : 'none'};
  justify-content: flex-start;

  width: 54rem;
  margin-top: 5rem;

  font-size: 4rem;
`;

const headerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  height: 8rem;
  margin-bottom: 2rem;

  font-weight: bold;
`;

const buttonStyle = ({ colors }: Theme) => css`
  width: 8rem;
  height: 8rem;

  background: transparent;

  color: ${colors.GRAY_700};

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }
`;

const contentStyle = css`
  display: flex;
  flex-direction: column;
  gap: 2rem;

  width: 100%;
`;

const itemStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  height: 8rem;
`;

const nameStyle = css`
  overflow: hidden;
  position: relative;

  width: 32rem;

  white-space: nowrap;
  text-overflow: ellipsis;

  &:hover {
    cursor: pointer;
  }
`;

const controlButtonsStyle = ({ flex }: Theme) => css`
  ${flex.row}
`;

export {
  buttonStyle,
  contentStyle,
  controlButtonsStyle,
  headerStyle,
  itemStyle,
  listStyle,
  nameStyle,
};
