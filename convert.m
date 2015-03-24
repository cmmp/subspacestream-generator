T = readtable('C:/Users/Cássio/Dropbox/workspace/subspacestream/subspace_stream.csv');
ndim = size(T,2);
X = cell2mat(table2cell(T(:,1:(ndim-1))));
rawsup = table2cell(T(:,ndim));
uclass = unique(rawsup);
sup = zeros(size(T, 1),1);

for i = 1:size(uclass,1)
    sup(strcmp(rawsup, uclass(i))) = i;
end

save 'subspace_stream.mat' X sup
