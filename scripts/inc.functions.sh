#!/usr/bin/env sh
#
# Collection of shared functions
#

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

approve() {
  printf "${GREEN}%s${NC}\n" "$*"
}

warn() {
  printf "${YELLOW}%s${NC}\n" "$*"
}

die() {
  printf "${RED}%s${NC}\n" "$*"
  exit 1
}

safe() {
  "$@"
  _status=$?
  if [ ${_status} -ne 0 ]; then
    die "\nBUILD FAILED\nAfter invoking \"$*\"\n" >&2
  fi
  return ${_status}
}

sed2() {
  cmd="${1}"
  shift
  sed -i'.bak' "${cmd}" "${@}"
  for file in "${@}"; do
    rm "${file}.bak"
  done
}
