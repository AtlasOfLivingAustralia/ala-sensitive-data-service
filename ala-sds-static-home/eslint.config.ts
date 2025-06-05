import eslintJs from '@eslint/js';
import tseslint from 'typescript-eslint';
import globals from 'globals'
import pluginReactHooks from 'eslint-plugin-react-hooks'
import pluginReactRefresh from 'eslint-plugin-react-refresh'
import { fileURLToPath } from 'node:url';
import { dirname } from 'node:path';

// FlatConfigItem type can be imported from 'eslint' or 'typescript-eslint' if needed,
// but tseslint.config() handles much of the typing.

const currentModuleDir = dirname(fileURLToPath(import.meta.url));

export default tseslint.config(
  // 1. Global ignores
  {
    ignores: [
      'dist/', // Build output
      'eslint.config.ts', // This configuration file
      'vite.config.ts',   // Vite configuration file
      '**/*.config.js',   // Other JavaScript config files (e.g., postcss.config.js)
      // Add any other generated files or directories if necessary
    ],
  },

  // 2. Base ESLint recommended rules for JavaScript
  eslintJs.configs.recommended,

  // 3. TypeScript configurations
  // This applies @typescript-eslint/parser, @typescript-eslint/eslint-plugin,
  // and recommended (type-checked) rules to .ts, .tsx, .mts, .cts files.
  ...tseslint.configs.recommendedTypeChecked,

  // 4. React specific configurations (for .jsx and .tsx files)
  {
    files: ['**/*.{jsx,tsx}'],
    plugins: {
      'react-hooks': pluginReactHooks,
      'react-refresh': pluginReactRefresh,
    },
    rules: {
      // Apply recommended rules from eslint-plugin-react-hooks
      ...pluginReactHooks.configs.recommended.rules,
      // Configure eslint-plugin-react-refresh
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
    },
    languageOptions: {
      // Ensure browser globals are available in React components
      globals: {
        ...globals.browser,
      },
    },
    settings: {
      // Optional: specify React version, 'detect' usually works
      react: {
        version: 'detect',
      },
    },
  },

  // 5. Customizations and overrides for all relevant files (JS, JSX, TS, TSX)
  {
    files: ['**/*.{js,jsx,ts,tsx}'], // Apply to all JS/TS source files
    languageOptions: {
      globals: {
        ...globals.browser, // General browser globals for all source files
        // ...globals.node, // Add if Node.js globals are needed in some source files (e.g. vite.config.ts if linted)
      },
    },
    rules: {
      // Customize @typescript-eslint/no-unused-vars.
      // This rule is enabled by tseslint.configs.recommendedTypeChecked.
      // We are overriding its options here to match your previous 'no-unused-vars' config
      // and add common TypeScript best practices.
      '@typescript-eslint/no-unused-vars': [
        'error',
        {
          vars: 'all',
          varsIgnorePattern: '^[A-Z_]', // Matches original: varsIgnorePattern for unused variables
          args: 'after-used',
          argsIgnorePattern: '^_',      // Common practice for unused function arguments
          ignoreRestSiblings: true,
        },
      ],
    },
  },

  // 6. Specific configuration for TypeScript files to set `parserOptions.project`
  // This is essential for `tseslint.configs.recommendedTypeChecked` to work effectively.
  {
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      parserOptions: {
        project: true, // Automatically finds tsconfig.json relative to tsconfigRootDir
        tsconfigRootDir: currentModuleDir, // Assumes eslint.config.ts is at the project root (ala-sds-static-home)
      },
    },
  }
);
 