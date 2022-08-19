import { css, Theme } from '@emotion/react';

const listStyle = ({ flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.column}

  display: ${isSideBarOpen ? 'flex' : 'none'};
  justify-content: flex-start;

  width: 54rem;

  font-size: 4rem;
`;

const headerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  height: 8rem;

  font-weight: bold;
`;

const googleImportButtonStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: relative;

  width: 100%;
  height: 11rem;
  padding: 4rem;
  margin: 2rem 0 3rem;
  border-radius: 4px;
  border: 1px solid ${colors.GRAY_600};

  background: ${colors.WHITE};

  font-size: 4rem;
  color: ${colors.GRAY_600};

  &:hover {
    filter: none;
  }
`;

const googleImportTextStyle = css`
  width: 100%;
`;

const contentStyle = css`
  display: flex;
  flex-direction: column;
  gap: 2rem;

  width: 100%;
`;

const skeletonStyle = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 5rem;
`;

const skeletonListStyle = ({ flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.column};

  display: ${isSideBarOpen ? 'flex' : 'none'};
  justify-content: flex-start;

  width: 54rem;
  margin-top: 16rem;

  font-size: 4rem;
`;

export {
  contentStyle,
  googleImportButtonStyle,
  googleImportTextStyle,
  headerStyle,
  listStyle,
  skeletonStyle,
  skeletonListStyle,
};
