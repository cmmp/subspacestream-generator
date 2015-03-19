T = readtable('C:/Users/Cássio/work/workspace/subspacestream/subspace_stream.csv');
X = cell2mat(table2cell(T(:,1:4)));
rawsup = table2cell(T(:,5));
uclass = unique(rawsup);
sup = zeros(size(T, 1),1);

for i = 1:size(uclass,1)
    sup(strcmp(rawsup, uclass(i))) = i;
end

save 'subspace_stream.mat' X sup
